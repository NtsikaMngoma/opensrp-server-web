package org.opensrp.web.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.joda.time.DateTime;
import org.opensrp.domain.Manifest;
import org.opensrp.service.ManifestService;
import org.opensrp.util.TaskDateTimeTypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/rest/manifest")
public class ManifestResource {

    public static Gson gson = new GsonBuilder().registerTypeAdapter(DateTime.class, new TaskDateTimeTypeConverter())
            .serializeNulls().create();
    private static Logger logger = LoggerFactory.getLogger(ManifestResource.class.toString());
    private ManifestService manifestService;
    public static final String IDENTIFIER ="identifier";

  /*  @Autowired
    public ManifestResource(ManifestService manifestService) {
        this.manifestService = manifestService;
    }*/

    @Autowired
    public void setManifestService(ManifestService manifestService) {
        this.manifestService = manifestService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<String> get() {
        try {
            return new ResponseEntity<>(gson.toJson(
                    manifestService.getAllManifest()),
                    RestUtils.getJSONUTF8Headers(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

  @RequestMapping(value = "/{identifier}", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getManifestByUniqueId(@PathVariable(IDENTIFIER) String identifier) {
        try {
            return new ResponseEntity<>(gson.toJson(manifestService.getManifest(identifier)), RestUtils.getJSONUTF8Headers(),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
   }

    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<HttpStatus> create(@RequestBody String entity) {
        try {
            Manifest manifest = gson.fromJson(entity, Manifest.class);
            System.out.println("Manifest version " + manifest.getAppVersion());
            manifestService.addManifest(manifest);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (JsonSyntaxException e) {
            logger.error("The request doesn't contain a valid manifest representation" + entity);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<HttpStatus> update(@RequestBody String entity) {
        try {
            Manifest manifest = gson.fromJson(entity, Manifest.class);
            manifestService.updateManifest(manifest);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (JsonSyntaxException e) {
            logger.error("The request doesn't contain a valid manifest representation" + entity);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> batchSave(@RequestBody String entity) {
        try {
            Type listType = new TypeToken<List<Manifest>>() {
            }.getType();
            List<Manifest> manifests = gson.fromJson(entity, listType);
            Set<String> tasksWithErrors = manifestService.saveManifests(manifests);
            if (tasksWithErrors.isEmpty())
                return new ResponseEntity<>("All Tasks  processed", HttpStatus.CREATED);
            else
                return new ResponseEntity<>(
                        "Tasks with identifiers not processed: " + String.join(",", tasksWithErrors),
                        HttpStatus.CREATED);
        } catch (JsonSyntaxException e) {
            logger.error("The request doesn't contain a valid manifest representation" + entity);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> delete(@RequestBody String entity) {
        try {
            Manifest manifest = gson.fromJson(entity, Manifest.class);
            manifestService.deleteManifest(manifest);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (JsonSyntaxException e) {
            logger.error("The request doesn't contain a valid manifest representation" + entity);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }  catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }   catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "appId/{appId}", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> getManifestByAppId(@PathVariable("appId") String appId) {
        try {
            return new ResponseEntity<>(gson.toJson(manifestService.getManifestByAppId(appId)), RestUtils.getJSONUTF8Headers(),
                    HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}